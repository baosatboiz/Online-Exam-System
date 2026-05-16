import "./index.css";
export default function MediaContent({data}){
    if(!data) 
        return(
    <div className="d-flex gap-3 justify-content-center">
        <div className="spinner-border"></div>
        <span>Loading</span>
    </div>)
    const normalize = ()=>{
        const raw = data.imageUrl;
        const bracket = raw.indexOf('[');
        if(bracket==-1) return [data.imageUrl];
        const baseUrl = raw.substring(0,bracket);
        const listImageJson = JSON.parse(raw.substring(bracket));
        return listImageJson.map(i=>baseUrl+i);
        }
    return (
        <div className="custom-scroll overflow-auto h-100 shadow-sm">
        <div className="">
            {data.imageUrl&&
            <div className="">{normalize().map(imgUrl=>
            <img src={imgUrl} className="img-fluid objaect-fit-contain" style={{maxHeight:"480px"}}/>
            )}
            </div>}
            {data.audioUrl&&
            <div className="p-3">
            <audio controls src={data.audioUrl}></audio>
            </div>}
        </div>
        </div>
    )
}