import { Credentials } from "./Credentials"
import { Profile } from "./Profile"

export interface RegisterRequest {
	profile:        Profile,
    credentials:    Credentials,
    is_admin:       boolean
}