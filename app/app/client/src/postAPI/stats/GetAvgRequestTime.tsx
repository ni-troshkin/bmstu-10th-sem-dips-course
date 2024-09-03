import axiosBackend from ".."
import { AllAvgRequestTimeResp } from "..";

const GetAvgRequestTime = async function(): Promise<AllAvgRequestTimeResp> {
    const response = await axiosBackend
        .get("/stats/requests/avgtime");

    return {
        status: response.status,
        content: response.data
    };
}

export default GetAvgRequestTime
