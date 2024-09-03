import axiosBackend, { GetBooksInLibraryResp } from ".."

const GetBooksInLibrary = async function(
    uuid: number,
    page: string, 
    size: string,
    showAll: boolean
): Promise<GetBooksInLibraryResp> {
    const response = await axiosBackend
        .get(`/libraries/${uuid}/books?page=${page}&size=${size}&showAll=${showAll}`);

    return {
        status: response.status,
        content: response.data
    };
}

export default GetBooksInLibrary