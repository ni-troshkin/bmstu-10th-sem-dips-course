import axiosBackend, { DefaultHeadersResp } from ".."

const Authorize = async function(): Promise<DefaultHeadersResp> {
    const response = await axiosBackend
        .post("/oauth/authorize?client_id=library-client");

    return {
        status: response.status,
        content: response.data,
        headers: response.headers
    };
}

export default Authorize
