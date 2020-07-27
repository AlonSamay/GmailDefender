import { Injectable } from "@angular/core";
import { Observable, throwError, of } from "rxjs";
import {
  HttpClient,
  HttpHeaders,
  HttpResponse,
  HttpParams,
} from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { Filter, User, FilterBoundary } from "src/app/app.model";
import { SocialUser } from "angularx-social-login";

@Injectable({
  providedIn: "root",
})
export class LocalServerService {
  private filtersURL = "/api/gmailDefender/filters/";
  private usersUrl = "/api/gmailDefender/users/";

  public clientId =
    "565000187130-9kk15bhnmp1k053ljjfvcjkj80de9b4e.apps.googleusercontent.com";
  public redirectUri = "http://localhost:8085/login/gmailCallback";

  authUser$: Observable<User>;
  authUser: User;

  httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
      Authorization: "AIzaSyBCccrl27S_MLmDSOwxkYNHBL0VRs770IA",
    }),
  };

  constructor(private httpClient: HttpClient) {}

  redirectLoginFlow(): Promise<User> {
    var url = "/api/login/gmail";

    this.authUser$ = this.httpClient
      .get<User>(url, this.httpOptions)
      .pipe(catchError(this.handleError));

    var promise = this.authUser$.toPromise();
    promise.then((user) => {
      this.authUser = user;
    });
    return promise;
  }

  sendAuthorizationCode(code: string) {
    let url = "/api/auth";
    let headers = new HttpHeaders({
      "Content-Type": "application/json",
      Authorization: "AIzaSyBCccrl27S_MLmDSOwxkYNHBL0VRs770IA",
    });
    console.log(code);
    let params = new HttpParams().set("code", code);

    this.httpClient
      .post<any>(url, { headers: headers, observe: "response", params: params })
      .pipe(catchError(this.handleError))
      .subscribe((email) => console.log(email));
  }

  retrieveToken(code) {
    let params = new URLSearchParams();
    params.append("grant_type", "authorization_code");
    params.append("client_id", this.clientId);
    params.append("redirect_uri", this.redirectUri);
    params.append("code", code);
    let headers = new HttpHeaders({
      "Content-type": "application/x-www-form-urlencoded; charset=utf-8",
      Authorization: "Basic " + btoa(this.clientId + ":secret"),
    });
    this.httpClient
      .post("/api/login/gmailCallback", params.toString(), { headers: headers })
      .subscribe(
        (data) => console.log("Code Sent"),
        (err) => alert("Invalid Credentials")
      );
  }

  logout() {
    return this.httpClient
      .post<any>("/api/logout", {
        headers: new HttpHeaders().set("Content-Type", "application/json"),
        responseType: "text",
      })
      .pipe(catchError(this.handleError));
  }

  getAllFilters(): Observable<Filter[]> {
    var url = "/api/gmailDefender/getAllFiltersByUsers";
    var options = {
      params: { userEmail: this.authUser.userEmail },
      headers: {
        "Content-Type": "application/json",
        Authorization: "AIzaSyBCccrl27S_MLmDSOwxkYNHBL0VRs770IA",
      },
    };
    return this.httpClient
      .get<Filter[]>(url, options)
      .pipe(catchError(this.handleError));
  }

  addFilter(filter: FilterBoundary): Observable<any> {
    const url = this.usersUrl + "addFilter/" + this.authUser.userEmail;
    const add$ = this.httpClient
      .post<Filter>(url, filter, this.httpOptions)
      .pipe(catchError(this.handleError));
    add$.subscribe(() => console.log("filter added"));
    return add$;
  }

  updateFilter(filter: Filter) {
    const url = this.filtersURL + filter.filterId;
    const update$ = this.httpClient
      .put(url, filter, this.httpOptions)
      .pipe(catchError(this.handleError));
    update$.subscribe(() => console.log("filter updated"));
    return update$;
  }

  deleteFilter(filter: Filter) {
    const url = this.filtersURL + "delete/" + filter.filterId;
    const delete$ = this.httpClient
      .delete<any>(url)
      .pipe(catchError(this.handleError));
    delete$.subscribe(() => console.log("filter deleted"));
    return delete$;
  }

  getAuthUser(): Promise<User> {
    const url = this.usersUrl + "auth";

    this.authUser$ = this.httpClient
      .get<User>(url)
      .pipe(catchError(this.handleError));

    var promise = this.authUser$.toPromise();

    promise.then((user) => {
      if (user) {
        this.authUser = user;
        console.log("user email:" + user.userEmail);
      }
    });
    
    return promise;
  }

  private handleError(error) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error("An error occurred:", error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` + `body was: ${error.error}`
      );
    }
    // return an observable with a user-facing error message
    return throwError("Something bad happened; please try again later.");
  }
}
