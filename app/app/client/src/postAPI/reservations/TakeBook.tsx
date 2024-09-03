import axiosBackend, { DefaultResp } from ".."
import { TakeBookRequest } from "types/TakeBookRequest";

const TakeBook = async function(
    libraryUid: number,
    bookUid: number,
    tillDate: string
): Promise<any> {
    let req: TakeBookRequest = {
        bookUid: bookUid,
        libraryUid: libraryUid,
        tillDate: tillDate
    }
    const response = await axiosBackend
        .post(`/reservations`, req);

    return {
        status: response.status,
        content: response.data
    };
}

export default TakeBook
