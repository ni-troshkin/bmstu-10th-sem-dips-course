import { Library } from "./Library";

export interface LibraryResponse {
	page:           number,
	pageSize:       number,
	totalElements:  number,
	items:  		Library[]
}