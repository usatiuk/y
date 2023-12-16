import "./ProfileCard.scss";
import { Link } from "react-router-dom";

export function ProfileCard({
    username,
    fullName,
}: {
    username: string;
    fullName: string;
}) {
    return (
        <div className={"profileCard"}>
            <Link to={`/home/profile/${username}`} className={"fullName"}>
                {fullName}
            </Link>
            <Link to={`/home/profile/${username}`} className={"username"}>
                {username}
            </Link>
        </div>
    );
}
