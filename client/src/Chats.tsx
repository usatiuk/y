import { Link, useLoaderData } from "react-router-dom";
import { chatListLoader, LoaderToType } from "./loaders";
import { isError } from "./api/dto";

import "./Chats.scss";

export function Chats() {
    const loaderData = useLoaderData() as LoaderToType<typeof chatListLoader>;
    if (!loaderData || isError(loaderData)) {
        return <div>error</div>;
    }

    return (
        <div className={"chats"}>
            <div className={"chatsHeader"}>
                <span>Your chats</span>
                <Link to={"../messages/chats/new"}>create</Link>
            </div>
            <div className={"chatsList"}>
                {loaderData.map((c) => (
                    <Link to={"../messages/chat/" + c.id} key={c.id}>
                        {c.name}
                    </Link>
                ))}
            </div>
        </div>
    );
}
