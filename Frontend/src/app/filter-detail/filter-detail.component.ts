import { Component, OnInit, Input } from "@angular/core";
import { Filter, FilterBoundary } from "src/app/app.model";
import { LocalServerService } from "src/app/local-server-service/local-server.service";
import { FormControl, Validators } from "@angular/forms";
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: "app-filter-detail",
  templateUrl: "./filter-detail.component.html",
  styleUrls: ["./filter-detail.component.css"],
})
export class FilterDetailComponent implements OnInit {
  constructor(
    private serivce: LocalServerService,
    public snackBar: MatSnackBar
  ) {}

  @Input() filter: Filter;

  save(filter: Filter): void {
    this.serivce.updateFilter(filter).subscribe(() => this.openSnackbar("Filter Saved!"));
  }

  openSnackbar(message: string) {
    this.snackBar.open(message, null, { duration: 3000 });
  }

  ngOnInit() {}
}
