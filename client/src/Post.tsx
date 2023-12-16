import "./Post.scss";

export function Post({
    text,
    createdDate,
}: {
    text: string;
    createdDate: string;
}) {
    return (
        <div className={"post"}>
            <span className={"text"}>{text}</span>
            <span className={"createdDate"}>{createdDate}</span>
        </div>
    );
}
