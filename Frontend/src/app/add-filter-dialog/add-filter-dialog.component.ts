import { Component, OnInit, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Filter, FilterBoundary } from "../app.model";
import { LocalServerService } from "../local-server-service/local-server.service";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: "app-add-filter-dialog",
  templateUrl: "./add-filter-dialog.component.html",
  styleUrls: ["./add-filter-dialog.component.css"],
})
export class AddFilterDialogComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<AddFilterDialogComponent>,
    private serivce: LocalServerService,
    public snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {}

  submit(filter: Filter): void {
    console.log("add-filter");
    this.serivce
      .addFilter(filter)
      .subscribe(() => this.openSnackbar("Filter Added!"));
    this.dialogRef.close(true);
  }

  openSnackbar(message: string) {
    this.snackBar.open(message, null, { duration: 3000 });
  }
}
