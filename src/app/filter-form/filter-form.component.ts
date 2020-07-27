import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { ReactiveFormsModule, FormControl, Validators } from "@angular/forms";
import { FilterBoundary, Filter } from "../app.model";

@Component({
  selector: "app-filter-form",
  templateUrl: "./filter-form.component.html",
  styleUrls: ["./filter-form.component.css"],
})
export class FilterFormComponent implements OnInit {
  @Output() filterEvent = new EventEmitter<Filter>();

  @Input() filter?: Filter;

  formControl = new FormControl("", [
    Validators.required,
    Validators.email,
  ]);

  constructor() {}

  ngOnInit() {
    if (!this.filter) {
      this.filter = {
        filterId: "",
        userFilterId: "",
        filterName: "",
        from: "",
        hasTheWords: "",
        subject: "",
      };
    }
  }

  submit() {
    console.log("filter-form");
    this.filterEvent.emit(this.filter);
  }
}
