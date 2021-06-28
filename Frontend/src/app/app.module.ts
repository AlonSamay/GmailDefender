import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";

import {MatListModule} from '@angular/material/list';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {MatSnackBarModule} from '@angular/material/snack-bar';

import { HttpClientModule } from "@angular/common/http";

import { FilterComponent } from "./filters/filters.component";
import { FilterDetailComponent } from "./filter-detail/filter-detail.component";
import { FilterFormComponent } from "./filter-form/filter-form.component";

import { AuthenticationService } from "./auth-service/authentication.service";

import { SocialLoginModule, AuthServiceConfig, LoginOpt, } from "angularx-social-login";
import { GoogleLoginProvider } from "angularx-social-login";

import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { AddFilterDialogComponent } from './add-filter-dialog/add-filter-dialog.component';

const googleLoginOptions: LoginOpt = {
  fetch_basic_profile: true,
  scope: 'https://mail.google.com/',
  offline_access: true,
  cookie_policy: 'single_host_origin',
  ux_mode: 'redirect',
  redirect_uri: 'http://localhost:8085/login/gmailCallback',
};


@NgModule({
  declarations: [
    AppComponent,
    FilterComponent,
    FilterDetailComponent,
    FilterFormComponent,
    AddFilterDialogComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    CommonModule,
    SocialLoginModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,

    MatListModule,
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  entryComponents: [
    FilterFormComponent
  ],


  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
