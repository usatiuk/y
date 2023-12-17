import "./Profile.scss";
import { Form, Link, useLoaderData } from "react-router-dom";
import { LoaderToType, profileLoader } from "./loaders";
import { isError } from "./api/dto";
import { Post } from "./Post";
import { useHomeContext } from "./HomeContext";
import { PostList } from "./PostList";

export interface IProfileProps {
    self: boolean;
}

export function Profile({ self }: IProfileProps) {
    const loaderData = useLoaderData() as LoaderToType<typeof profileLoader>;

    const homeContext = useHomeContext();

    if (!loaderData || isError(loaderData)) {
        return <div>Error</div>;
    }

    return (
        <div className={"profileView"}>
            <div className={"profileInfo"}>
                <span className={"fullName"}>{homeContext.user.fullName}</span>
                <span className={"username"}>{homeContext.user.fullName}</span>
            </div>
            {self && (
                <div className={"newPost"}>
                    <Form method="post">
                        <textarea
                            placeholder={"Write something!"}
                            name="text"
                        />
                        <button name="intent" value="post" type="submit">
                            Post
                        </button>
                    </Form>
                </div>
            )}
            <PostList
                posts={loaderData.posts}
                selfUuid={homeContext.user.uuid}
            />
        </div>
    );
}
