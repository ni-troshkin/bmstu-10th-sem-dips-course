import { TokenRequest } from "types/TokenRequest";
import axiosBackend, { TokenResponseResp } from ".."

const Token = async function(req: TokenRequest): Promise<TokenResponseResp> {
    const response = await axiosBackend
        .post("/oauth/token", req);

    return {
        status: response.status,
        content: response.data
    };
}

export default Token
