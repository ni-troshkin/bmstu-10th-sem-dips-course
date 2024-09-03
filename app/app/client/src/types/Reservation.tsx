import { Book } from "./Book";
import { Library } from "./Library";

export interface Reservation {
	reservationUid:             number,
    status:                     string,
    startDate:                  string,
    tillDate:                   string,
    book:                       Book,
    library:                    Library
}