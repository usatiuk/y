import "./Profile.scss";
import { Form, Link, useLoaderData } from "react-router-dom";
import { LoaderToType, profileSelfLoader } from "./loaders";
import { isError } from "./api/dto";
import { ProfileCard } from "./ProfileCard";

export interface IProfileProps {
    self: boolean;
}

export function Profile(props: IProfileProps) {
    const loaderData = useLoaderData() as LoaderToType<
        typeof profileSelfLoader
    >;

    if (!loaderData || isError(loaderData)) {
        return <div>Error</div>;
    }
    return (
        <div className={"profileView"}>
            <div className={"profileInfo"}>
                <span className={"fullName"}>{loaderData.user.fullName}</span>
                <span className={"username"}>{loaderData.user.fullName}</span>
            </div>
            <div className={"newPost"}>
                <Form method="post">
                    <textarea placeholder={"Write something!"} name="text" />
                    <button type="submit">Post</button>
                </Form>
            </div>
            <div className={"posts"}>
                {loaderData.posts &&
                    loaderData.posts.map((p) => {
                        return (
                            <div key={p.id} className={"post"}>
                                {p.text}
                            </div>
                        );
                    })}
            </div>
        </div>
    );
}
