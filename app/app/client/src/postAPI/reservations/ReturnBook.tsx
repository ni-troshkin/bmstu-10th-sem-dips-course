import { ReturnBookRequest } from "types/ReturnBookRequest";
import axiosBackend, { DefaultResp } from ".."

const ReturnBook = async function(
    uid: number,
    req: ReturnBookRequest
): Promise<DefaultResp> {
    const response = await axiosBackend
        .post(`/reservations/${uid}/return`, req);

    return {
        status: response.status,
        content: response.data
    };
}

export default ReturnBook
