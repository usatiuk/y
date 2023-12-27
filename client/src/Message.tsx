import "./Message.scss";
import { TChatTo, TMessageTo } from "./api/dto";
import { Form, Link, useNavigation, useSubmit } from "react-router-dom";
import { useState } from "react";

export function Message({
    message,
    chat,
    actions,
}: {
    message: TMessageTo;
    chat: TChatTo;
    actions: boolean;
}) {
    const [editing, setEditing] = useState(false);
    const submit = useSubmit();
    const navigation = useNavigation();
    const busy = navigation.state === "submitting";

    if (editing) {
        return (
            <div className={"message messageEditing"}>
                <Form className={"postForm"} method="patch">
                    <input
                        hidden={true}
                        name={"messageId"}
                        value={message.id}
                    />
                    <textarea
                        placeholder={"Write something!"}
                        name="text"
                        defaultValue={message.contents}
                    />
                    <button
                        name="intent"
                        value="updateMessage"
                        type="submit"
                        onClick={(e) => {
                            setEditing(false);
                            submit(e.currentTarget);
                        }}
                        disabled={busy}
                    >
                        Save
                    </button>
                </Form>
            </div>
        );
    }

    return (
        <div className={"message"}>
            <span className={"text"}>{message.contents}</span>
            <div className={"footer"}>
                <div className={"info"}>
                    <span className={"createdDate"}>
                        {new Date(message.createdAt * 1000).toUTCString()}
                    </span>
                    <Link
                        className={"authorLink"}
                        to={"/home/profile/" + message.authorUsername}
                    >
                        by {message.authorUsername}
                    </Link>
                </div>
                {actions && (
                    <div className={"actions"}>
                        {<button onClick={() => setEditing(true)}>edit</button>}
                        <Form method={"delete"}>
                            <input
                                hidden={true}
                                name={"messageToDeleteId"}
                                value={message.id}
                            />
                            <button
                                name="intent"
                                value="deleteMessage"
                                type={"submit"}
                                disabled={busy}
                            >
                                delete
                            </button>
                        </Form>
                    </div>
                )}
            </div>
        </div>
    );
}
