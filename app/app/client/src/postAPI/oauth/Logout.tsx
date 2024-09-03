import axiosBackend, { DefaultResp } from ".."

const Logout = async function(username: string): Promise<DefaultResp> {
    const response = await axiosBackend
        .post(`/oauth/logout?username=${username}`);

    return {
        status: response.status,
        content: response.data
    };
}

export default Logout
