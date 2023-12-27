import {
    Link,
    useFetcher,
    useLoaderData,
    useRevalidator,
} from "react-router-dom";
import { chatLoader, LoaderToType } from "./loaders";
import { isError } from "./api/dto";

import "./Chat.scss";
import "./PostForm.scss";
import { Message } from "./Message";
import { useEffect } from "react";
import { getTokenUserUuid } from "./api/utils";

export function Chat() {
    const loaderData = useLoaderData() as LoaderToType<typeof chatLoader>;

    if (!loaderData) {
        return <div>error</div>;
    }

    const chat = loaderData.chat;
    const messages = loaderData.messages;
    if (!chat || isError(chat) || !messages || isError(messages)) {
        return <div>error</div>;
    }

    const revalidator = useRevalidator();

    useEffect(() => {
        const interval = setInterval(() => revalidator.revalidate(), 1000);
        return () => {
            clearInterval(interval);
        };
    }, []);

    const fetcher = useFetcher();
    const busy = fetcher.state === "submitting";

    const sortedMessages = messages.sort((a, b) => b.createdAt - a.createdAt);

    return (
        <div className={"chat"}>
            <div className={"chatHeader"}>
                <span className={"chatHeaderName"}>{chat.name}</span>
                {chat.creatorUuid == getTokenUserUuid() && (
                    <span className={"chatHeaderActions"}>
                        <Link to={"edit"}>edit</Link>
                    </span>
                )}
            </div>
            <fetcher.Form className={"messageForm postForm"} method="post">
                <textarea placeholder={"Write something!"} name="text" />
                <input hidden={true} value={chat.id} name={"chatId"} />
                <button
                    name="intent"
                    value="addMessage"
                    type="submit"
                    disabled={busy}
                >
                    Write
                </button>
            </fetcher.Form>
            <div className={"messages"}>
                {sortedMessages.map((m) => (
                    <Message
                        key={m.id}
                        message={m}
                        chat={chat}
                        actions={m.authorUuid == getTokenUserUuid()}
                    />
                ))}
            </div>
        </div>
    );
}
