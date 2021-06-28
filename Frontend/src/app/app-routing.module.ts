import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { FilterComponent } from "./filters/filters.component";
import { FilterDetailComponent } from "./filters/filter-detail/filter-detail.component";

const routes: Routes = [
  { path: "", redirectTo: "/filters", pathMatch: "full" },
  { path: "filters", component: FilterComponent },
  { path: "detail/:id", component: FilterDetailComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
