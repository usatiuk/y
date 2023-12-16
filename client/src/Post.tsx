import "./Post.scss";
import { Form } from "react-router-dom";

export function Post({
    text,
    createdDate,
    actions,
    id,
}: {
    text: string;
    createdDate: string;
    actions: boolean;
    id: number;
}) {
    return (
        <div className={"post"}>
            <span className={"text"}>{text}</span>
            <div className={"footer"}>
                <div className={"info"}>
                    <span className={"createdDate"}>{createdDate}</span>
                </div>
                {actions && (
                    <div className={"actions"}>
                        <Form method={"delete"}>
                            <input
                                hidden={true}
                                name={"postToDeleteId"}
                                value={id}
                            />
                            <button
                                name="intent"
                                value="deletePost"
                                type={"submit"}
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
