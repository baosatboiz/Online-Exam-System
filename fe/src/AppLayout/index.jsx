import ExamHeader from "../ExamHeader";
import ExamLayout from "../ExamLayout";
import Pagination from "../Pagination";
import { TimeProvider } from "../TimeProvider";

export default function AppLayout(){
    return (
        <div className="min-vh-100 bg-light">
            <TimeProvider>
            <div><ExamHeader/></div>
            </TimeProvider>
            <div className="my-5 py-5"><ExamLayout/></div>
            <div><Pagination/></div>
        </div>
    )
}