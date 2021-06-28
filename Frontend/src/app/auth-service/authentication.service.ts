import { Injectable } from "@angular/core";

import { Observable } from "rxjs";

import { AuthService, SocialUser } from "angularx-social-login";
import { GoogleLoginProvider } from "angularx-social-login";
import { LocalServerService } from "../local-server-service/local-server.service";

@Injectable({
  providedIn: "root",
})
export class AuthenticationService {
  private user: SocialUser;
  private loggedIn: boolean;

  constructor(
    private authService: AuthService,
    private localServer: LocalServerService
  ) {}

  initial() {
    if (this.loggedIn) return;

    this.authService.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = user != null;
    });
  }

  login() {
    this.authService
      .signIn(GoogleLoginProvider.PROVIDER_ID)
      .then((user) => {
        // this.localServer.sendAuthorizationCode(user.authorizationCode);
        // this.localServer.retrieveToken(user.authorizationCode);
        console.log(user);
      })
      .then(() => this.initial());
  }

  isUserLogged(): boolean {
    return this.loggedIn;
  }

  logout() {
    this.authService.signOut();
  }

  getUser(): Observable<SocialUser> {
    return this.authService.authState;
  }
}
