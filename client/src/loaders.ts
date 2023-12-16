import { getSelf } from "./api/Person";

export async function homeLoader() {
    return await getSelf();
}
