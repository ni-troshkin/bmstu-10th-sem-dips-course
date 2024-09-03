import { Book } from "./Book"
import { Library } from "./Library"
import { UserRatingResponse } from "./UserRatingResponse"

export interface TakeBookResponse {
    reservationUid: number
    status:         string,
    startDate:      string,
    tillDate:       string,
    book:           Book,
    library:        Library,
    rating:         UserRatingResponse
}