import { useSession } from "../ExamDynamicProvider";
import MediaContent from "../MediaContent";
import QuestionGroup from "../QuestionGroup";

export default function ExamLayout(){
    const {currentItem,currentIndex} = useSession();
    console.log(currentItem);
    return (
        <div className="container">
            <div className="row align-items-start">
                <div className="col-12 col-md-6 sticky-md-top" style={{top:"80px"}}><MediaContent data={currentItem}/></div>
                <div className="col-12 col-md-6">
                    <QuestionGroup/>
                </div>
            </div>
        </div>
    )
}