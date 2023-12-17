import { TPersonTo } from "./api/dto";
import { useOutletContext } from "react-router-dom";

export type HomeContextType = {
    user: TPersonTo;
};

export function useHomeContext() {
    return useOutletContext<HomeContextType>();
}
