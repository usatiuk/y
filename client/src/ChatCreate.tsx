import { Form, useLoaderData, useNavigation } from "react-router-dom";
import { LoaderToType, newChatLoader } from "./loaders";
import { isError } from "./api/dto";
import { getTokenUserUuid } from "./api/utils";

export function ChatCreate() {
    const loaderData = useLoaderData() as LoaderToType<typeof newChatLoader>;
    if (!loaderData || isError(loaderData)) {
        return <div>error</div>;
    }

    const navigation = useNavigation();
    const busy = navigation.state === "submitting";

    return (
        <div className={"chatsNew"}>
            <Form method="post">
                <label htmlFor="fname">name:</label>
                <input type="text" name="name" />
                <select multiple name={"members"}>
                    {loaderData
                        .filter((p) => p.uuid != getTokenUserUuid())
                        .map((p) => (
                            <option value={p.uuid}>{p.username}</option>
                        ))}
                </select>

                <button type="submit" disabled={busy}>
                    Create
                </button>
            </Form>
        </div>
    );
}
