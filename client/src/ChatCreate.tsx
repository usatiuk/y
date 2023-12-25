import {
    Form,
    useActionData,
    useLoaderData,
    useNavigation,
} from "react-router-dom";
import { LoaderToType, newChatLoader } from "./loaders";
import { isError } from "./api/dto";
import { getTokenUserUuid } from "./api/utils";

import "./ChatCreate.scss";
import { ActionToType, newChatAction } from "./actions";

export function ChatCreate() {
    const loaderData = useLoaderData() as LoaderToType<typeof newChatLoader>;
    if (!loaderData || isError(loaderData)) {
        return <div>error</div>;
    }
    let errors: JSX.Element[] = [];

    const actionData = useActionData() as ActionToType<typeof newChatAction>;

    if (isError(actionData)) {
        errors = actionData.errors.map((e) => {
            return <a>{e}</a>;
        });
    }

    const navigation = useNavigation();
    const busy = navigation.state === "submitting";

    return (
        <div className={"chatsNew"}>
            <div className={"errors"}>{errors}</div>
            <Form method="post">
                <label htmlFor="fname">Chat name:</label>
                <input type="text" name="name" />
                <label htmlFor="members">Members:</label>
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
