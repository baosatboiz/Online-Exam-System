import ExamHeader from "../ExamHeader";
import ExamLayout from "../ExamLayout";
import Pagination from "../Pagination";
import { TimeProvider } from "../TimeProvider";

export default function AppLayout(){
    return (
        <div>
            <TimeProvider>
            <div><ExamHeader/></div>
            </TimeProvider>
            <div className="mt-5 pt-5"><ExamLayout/></div>
            <div><Pagination/></div>
        </div>
    )
}