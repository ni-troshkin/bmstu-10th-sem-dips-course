import { LibraryBook } from "./LibraryBook";

export interface LibraryBookResponse {
	page:           number,
	pageSize:       number,
	totalElements:  number,
	items:  		LibraryBook[]
}