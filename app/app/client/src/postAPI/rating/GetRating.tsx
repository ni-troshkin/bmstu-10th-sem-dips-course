import axiosBackend, { UserRatingResp } from ".."

const GetRating = async function(): Promise<UserRatingResp> {
    const response = await axiosBackend
        .get("/rating");

    return {
        status: response.status,
        content: response.data
    };
}

export default GetRating
