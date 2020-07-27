import { Component, OnInit, NgZone, ViewChild } from "@angular/core";
import { AuthenticationService } from "./auth-service/authentication.service";
import { Observable, from, interval, Subscribable, Subscription } from "rxjs";
import { User } from "./app.model";
import { LocalServerService } from "./local-server-service/local-server.service";
import { SocialUser } from "angularx-social-login";
import { MatDialog } from "@angular/material/dialog";
import { FilterFormComponent } from "./filter-form/filter-form.component";
import { AddFilterDialogComponent } from "./add-filter-dialog/add-filter-dialog.component";
import { FilterComponent } from "./filters/filters.component";
import { MatSnackBar } from "@angular/material/snack-bar";
import { IfStmt } from "@angular/compiler";
import { takeUntil, takeWhile } from "rxjs/operators";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent implements OnInit {
  @ViewChild(FilterComponent) filterCmp: FilterComponent;
  title = "Gmail Defender";
  isLogged: boolean = false;

  subscription: Subscription;

  constructor(
    public authService: AuthenticationService,
    public localServerService: LocalServerService,
    public dialog: MatDialog,
    public snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {}

  login() {
    var user$ = this.localServerService.redirectLoginFlow();
    user$.then((user) => {
      if (user && user != null) {
        this.isLogged = true;
        this.openSnackbar(user.userEmail + " logged in!");
      } else {
        var source = interval(10000);
        this.subscription = source
          .pipe(takeWhile(() => !this.isLogged))
          .subscribe(() => {
            this.fetchUser();
          });
      }
    });
  }

  fetchUser() {
    this.localServerService.getAuthUser().then((user) => {
      if (user) {
        this.isLogged = true;
        this.openSnackbar(user.userEmail + " logged in!");
      }
    });
  }

  logout() {
    this.localServerService.logout().subscribe(() => {
      this.isLogged = false;
      this.openSnackbar("Logged out!");
    });
    // this.authService.logout();
    // this.isLogged = false;
    //   this.openSnackbar("Logged out!");
  }

  openAddFilterDialog() {
    this.dialog
      .open(AddFilterDialogComponent)
      .afterClosed()
      .subscribe(() => {
        this.filterCmp.getFilters();
        this.openSnackbar("Filter Added!");
      });
  }

  openSnackbar(message: string) {
    this.snackBar.open(message, null, { duration: 3000 });
  }
}
