async function get1(bno) { // 함수 내부에서 await 사용 가능 . 항상 Promise 반환
    const result = await axios.get(`/replies/list/${bno}`) // HTTP GET 요청 보내는 함수
    //promise는 get1 함수가 실행이 되면 어떠한 결과를 나오는지 보여주기위함.
    //서버 응답이 올 때까지 기다림. 비동기 호출
    // console.log(result)
    // return result.data;
    return result
}


async function getList({bno, page, size, goLast}){
    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})
    return result.data
}