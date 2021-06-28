import { Component, OnInit } from "@angular/core";
import { MatListModule } from "@angular/material/list";
import { LocalServerService } from "../local-server-service/local-server.service";

import { Filter, FilterBoundary } from "src/app/app.model";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: "app-filters",
  templateUrl: "./filters.component.html",
  styleUrls: ["./filters.component.css"],
})
export class FilterComponent implements OnInit {
  filters: Filter[];

  selectedFilter: Filter;

  constructor(
    private filterService: LocalServerService,
    public snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.getFilters();
  }

  onSelect(filter: Filter) {
    if (filter != this.selectedFilter) this.selectedFilter = filter;
    else this.selectedFilter = undefined;
  }

  edit() {
    if (this.selectedFilter) {
      this.filterService.updateFilter(this.selectedFilter).subscribe(() => {
        this.getFilters();
        this.openSnackbar("Message Edited!");
      });
    }
  }

  delete() {
    if (this.selectedFilter) {
      this.filterService.deleteFilter(this.selectedFilter).subscribe(() => {
        this.getFilters();
        this.openSnackbar("Message Deleted!");
      });
    }
  }

  getFilters() {
    this.filterService
      .getAllFilters()
      .subscribe((filters) => (this.filters = filters));
  }

  openSnackbar(message: string) {
    this.snackBar.open(message, null, { duration: 3000 });
  }
}
