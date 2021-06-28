export interface Filter {
  filterId: string;
  userFilterId: string;
  filterName: string;
  subject: string;
  from: string;
  hasTheWords: string;
}

export interface FilterBoundary {
  filterName: string;
  subject: string;
  from: string;
  hasTheWords: string;
}

export interface User {
  filterId: string;
  userEmail: string;
  userUid: string;
}
