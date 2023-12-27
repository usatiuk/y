import {
    Form,
    useActionData,
    useLoaderData,
    useNavigation,
} from "react-router-dom";
import { editChatLoader, LoaderToType } from "./loaders";
import { isError } from "./api/dto";
import { ActionToType, editChatAction } from "./actions";
import { getTokenUserUuid } from "./api/utils";

import "./ChatCreate.scss";

export function ChatEdit() {
    const loaderData = useLoaderData() as LoaderToType<typeof editChatLoader>;

    if (!loaderData) return <div>error</div>;

    const { people, chat, chatMembers } = loaderData;

    if (
        !people ||
        isError(people) ||
        !chat ||
        isError(chat) ||
        !chatMembers ||
        isError(chatMembers)
    ) {
        return <div>error</div>;
    }
    let errors: JSX.Element[] = [];

    const actionData = useActionData() as ActionToType<typeof editChatAction>;

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
            <Form method="patch">
                <input type={"hidden"} name={"chatId"} value={chat.id} />
                <label htmlFor="fname">Chat name:</label>
                <input type="text" name="name" defaultValue={chat.name} />
                <label htmlFor="members">Members:</label>
                <select
                    multiple
                    defaultValue={chatMembers.map((cme) => cme.uuid)}
                    name={"members"}
                >
                    {people
                        .filter((p) => p.uuid != getTokenUserUuid())
                        .map((p) => (
                            <option value={p.uuid}>{p.username}</option>
                        ))}
                </select>

                <button
                    type="submit"
                    name={"intent"}
                    value={"update"}
                    disabled={busy}
                >
                    Update
                </button>
            </Form>
            <Form method={"delete"}>
                <input type={"hidden"} name={"chatId"} value={chat.id} />
                <button
                    type="submit"
                    name={"intent"}
                    value={"delete"}
                    disabled={busy}
                >
                    Delete
                </button>
            </Form>
        </div>
    );
}
