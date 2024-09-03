import axiosBackend, { GetLibrariesByCityResp } from ".."

const GetLibrariesByCity = async function(
    city: string,
    page: number, 
    size: number
): Promise<GetLibrariesByCityResp> {
    const response = await axiosBackend
        .get(`/libraries?city=${city}&page=${page}&size=${size}`);

    return {
        status: response.status,
        content: response.data
    };
}

export default GetLibrariesByCity