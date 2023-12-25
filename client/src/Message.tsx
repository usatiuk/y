import "./Message.scss";
import { TChatTo, TMessageTo } from "./api/dto";
import { Link } from "react-router-dom";

export function Message({
    message,
    chat,
}: {
    message: TMessageTo;
    chat: TChatTo;
}) {
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
                {/*{actions && (*/}
                {/*    <div className={"actions"}>*/}
                {/*        {<button onClick={() => setEditing(true)}>edit</button>}*/}
                {/*        <Form method={"delete"}>*/}
                {/*            <input*/}
                {/*                hidden={true}*/}
                {/*                name={"postToDeleteId"}*/}
                {/*                value={id}*/}
                {/*            />*/}
                {/*            <button*/}
                {/*                name="intent"*/}
                {/*                value="deletePost"*/}
                {/*                type={"submit"}*/}
                {/*                disabled={busy}*/}
                {/*            >*/}
                {/*                delete*/}
                {/*            </button>*/}
                {/*        </Form>*/}
                {/*    </div>*/}
                {/*)}*/}
            </div>
        </div>
    );
}
