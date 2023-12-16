import "./ProfileCard.scss";

export function ProfileCard({
    username,
    fullName,
}: {
    username: string;
    fullName: string;
}) {
    return (
        <div className={"profileCard"}>
            <span className={"fullName"}>{fullName}</span>
            <span className={"username"}>{username}</span>
        </div>
    );
}
