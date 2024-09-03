import axiosBackend from ".."
import { AllStatsResp } from "..";

const GetAllStats = async function(): Promise<AllStatsResp> {
    const response = await axiosBackend
        .get("/stats");

    return {
        status: response.status,
        content: response.data
    };
}

export default GetAllStats
