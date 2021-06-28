package gmail.layout;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import gmail.aop.UserNotFoundException;
import gmail.data.FilterEntity;
import gmail.infra.UserFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.GmailScopes;

import gmail.data.UserFilterEntity;

@RestController
public class GoogleMailController {

    private static final String FROM = "From";
    private static final String APPLICATION_NAME = "Gmail Defender";
    private static final String SUBJECT = "Subject";

    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static Gmail client;

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    Profile authUserProfile;

    boolean isAuth = false;

    private final UserFilterService userFilterService;

    @Value("${gmail.client.clientId}")
    private String clientId;

    @Value("${gmail.client.clientSecret}")
    private String clientSecret;

    @Value("${gmail.client.redirectUri}")
    private String redirectUri;

    @Autowired
    public GoogleMailController(UserFilterService userFilterService) {
        super();
        this.userFilterService = userFilterService;
    }

    @GetMapping(value = "/login/gmail")
    public UserFilterBoundary googleConnectionStatus() throws Exception {
        if (client != null) {
            this.isAuth = true;
            return getAuthUser();
        }
        String authURL = authorize();
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + authURL);
        return null;
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout() {
        isAuth = false;
        return ResponseEntity.ok().build();
    }


    @GetMapping(path = "/gmailDefender/users/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserFilterBoundary getAuthUser() {
        if (!isAuth)
            return null;

        return new UserFilterBoundary(
                this.userFilterService.getUserFilter(authUserProfile.getEmailAddress()));
    }

    @GetMapping(value = "/login/gmailCallback", params = "code")
    public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code) throws IOException {
        TokenResponse response = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();
        credential = flow.createAndStoreCredential(response, "userID");

        client = new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        isAuth = true;

        String userId = "me";

        Gmail.Users.GetProfile userGetProfile = client
                .users()
                .getProfile(userId);

        authUserProfile = userGetProfile.execute();

        UserFilterEntity us = new UserFilterEntity();
        us.setEmail(authUserProfile.getEmailAddress());
        us.setToken(userGetProfile.getOauthToken());
        us.setHistoryId(authUserProfile.getHistoryId());
        us.setUserUid(userGetProfile.getUserId());

        System.out.println(userGetProfile.getKey());
        System.out.println(userGetProfile.getUserId());
        try {
            this.userFilterService.getUserFilter(authUserProfile.getEmailAddress());
            this.userFilterService.updateUserFilter(authUserProfile.getEmailAddress(), us);

        } catch (UserNotFoundException e) {
            this.userFilterService.newUserFilter(us);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return ResponseEntity.ok(authUserProfile.getEmailAddress());
    }

    private String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            Details web = new Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(GmailScopes.MAIL_GOOGLE_COM)).build();
        }

        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);

        System.out.println("gamil authorizationUrl ->" + authorizationUrl);
        return authorizationUrl.build();
    }

    public BigInteger getLastHistoryID() throws IOException {
        return client
                .users()
                .history()
                .list("me")
                .execute()
                .getHistoryId();
    }

    private List<Message> getNewMessages(UserFilterEntity entity) throws IOException {
        List<String> historyType = new ArrayList<>();
        historyType.add("messageAdded");
        historyType.add("messageDeleted");

        BigInteger startHistoryID = entity.getHistoryId();

        // get history of auth gmail user
        ListHistoryResponse response = client.users()
                .history()
                .list("me")
                .setStartHistoryId(startHistoryID)
                .setHistoryTypes(historyType)
                .execute();

        return findExistingMailsOnly(response, startHistoryID);
    }

    // messages which received from history may have duplicate values in case they were deleted.
    // this method is comparing between new messages and deleted messages,
    // and return only existing user's mailbox's mails.
    private List<Message> findExistingMailsOnly(ListHistoryResponse response, BigInteger historyID) throws IOException {
        List<Message> allDeleted = new ArrayList<>();
        List<Message> allNew = new ArrayList<>();

        // iterate on the response for added and deleted messages. add them to lists for comparision
        while (response.getHistory() != null) {
            for (History history : response.getHistory()) {
                if (history.getMessagesAdded() != null) {
                    allNew.addAll(history
                            .getMessagesAdded()
                            .stream()
                            .map(HistoryMessageAdded::getMessage)
                            .collect(Collectors.toList()));
                }
                if (history.getMessagesDeleted() != null) {
                    allDeleted.addAll(history
                            .getMessagesDeleted()
                            .stream()
                            .map(HistoryMessageDeleted::getMessage)
                            .collect(Collectors.toList()));
                }
            }

            // check for next history page
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = client.users()
                        .history()
                        .list("me")
                        .setPageToken(pageToken)
                        .setStartHistoryId(historyID)
                        .execute();
            } else {
                break;
            }
        }
        // remove any message which has deleted
        allNew.removeAll(allDeleted);
        return allNew;
    }

    public void deleteMail(Message message) throws IOException {
        client.users()
                .messages()
                .delete("me", message.getId())
                .execute();
    }

    public boolean checkArraysByAnd(String[] toCheckArr, String[] filterArr) {
        return Arrays.asList(toCheckArr).containsAll(Arrays.asList(filterArr));
    }

    public boolean aiCheck(CheckWithAI ai, String subject, String[] body) {
        StringBuilder strForAI = new StringBuilder("subject: " + subject);
        StringBuilder strCheckENLang = new StringBuilder();

        Arrays.stream(body).forEach(word -> {
            strForAI.append(" ").append(word);
            strCheckENLang.append(" ").append(word);
        });

        if (!StandardCharsets.US_ASCII.newEncoder().canEncode(strCheckENLang.toString())) {
            return false;
        }

        return ai.checkWithAI(strForAI.toString());
    }

    public boolean filterCheck(Set<FilterEntity> allFilters, String fromHeader, String subjectHeader, String[] body) {
        boolean delete = false;
        for (FilterEntity filter : allFilters) {
            if (fromHeader != null && fromHeader.toLowerCase().equals(filter.getFrom().toLowerCase())) {
                delete = true;
                break;
            }

            if (subjectHeader != null) {
                String[] filterSubjectArr = filter.getSubject().split("\\W+");
                String[] subjectArr = subjectHeader.split("\\W+");

                if (delete = checkArraysByAnd(subjectArr, filterSubjectArr)) {
                    break;
                }
            }

            String[] filterBodyArr = filter.getHasTheWords().split("\\W+");

            if (delete = checkArraysByAnd(body, filterBodyArr)) {
                break;
            }
        }
        return delete;
    }

    @Scheduled(fixedDelay = 20000)
    public synchronized void checkNewMails() throws IOException {
        if (!isAuth) {
            return;
        }

        System.out.println("checking new mails");

        UserFilterEntity userEntity = userFilterService.getUserFilter(authUserProfile.getEmailAddress());
        BigInteger newHistoryID = getLastHistoryID();

        if (newHistoryID.compareTo(userEntity.getHistoryId()) > 0) {
            Set<FilterEntity> allFilters = userEntity.getFilters();
            if (allFilters.isEmpty()) {
                System.out.println("No filters for user");
                return;
            }

            CheckWithAI ai = new CheckWithAI();

            List<Message> newMessages = getNewMessages(userEntity);

            String fromHeader;
            String subjectHeader;
            String[] body;

            for (Message message : newMessages) {
                fromHeader = null;
                subjectHeader = null;
                Message messageFromClient = client.users()
                        .messages()
                        .get("me", message.getId())
                        .execute();
                MessagePart payload = messageFromClient.getPayload();

                if (payload == null) {
                    continue;
                }

                for (MessagePartHeader header : payload.getHeaders()) {
                    String headerName = header.getName();
                    if (headerName.equals(FROM)) {
                        fromHeader = header.getValue();
                    } else if (headerName.equals(SUBJECT)) {
                        subjectHeader = header.getValue();
                    }
                }

                body = StringUtils.newStringUtf8(Base64.decodeBase64(payload
                        .getParts()
                        .get(0)
                        .getBody()
                        .getData())).split("\\W+");

                if (aiCheck(ai, subjectHeader, body)
                        || filterCheck(allFilters, fromHeader, subjectHeader, body)) {
                    deleteMail(message);
                    System.out.println("message " + message.getId() + " deleted");
                }
            }

            userEntity.setHistoryId(newHistoryID);
            userFilterService.updateUserFilter(userEntity.getEmail(), userEntity);
        }
    }
}