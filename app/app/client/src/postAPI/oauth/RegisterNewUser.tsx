import axiosBackend, { DefaultResp } from ".."
import { RegisterRequest } from "types/RegisterRequest";

const RegisterNewUser = async function(req: RegisterRequest): Promise<DefaultResp> {
    const response = await axiosBackend
        .post("/oauth/register", req);

    return {
        status: response.status,
        content: response.data
    };
}

export default RegisterNewUser
