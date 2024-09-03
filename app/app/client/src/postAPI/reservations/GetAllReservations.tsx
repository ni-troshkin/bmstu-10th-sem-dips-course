import axiosBackend, { AllReservationsResp } from ".."

const GetAllReservations = async function(): Promise<AllReservationsResp> {
    const response = await axiosBackend
        .get(`/reservations`);

    return {
        status: response.status,
        content: response.data
    };
}

export default GetAllReservations