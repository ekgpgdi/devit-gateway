# Devit
<p align="center"> 
<img src = 'https://user-images.githubusercontent.com/84092014/177942862-e4755aa7-f87b-4eaa-8eae-07bcaeb3932e.png' style='width:300px;'/>
</p>
경험이 많고 실력 있는 개발자에게 도움을 받기 위한 플랫폼입니다. <br/>
기업 또는 개인에게 알맞는 개발자의 스펙과 원하는 직무를 등록하여 구인하고 개발자는 확인 후 지원서를 넣어 서로가 만족하는 상황이 되었을 때 계약이 진행될 수 있도록 중개하는 웹 사이트입니다. <br/>

## Devit Architecture
<img width="1005" alt="스크린샷 2022-07-25 오후 12 34 59" src="https://user-images.githubusercontent.com/84092014/180694026-b0c51181-5ddc-4e84-b659-2d32d33e05eb.png">

## Devit Gateway
Devit 프로젝트 내 게이트웨이입니다. <br/>
요청이 들어오면 요청 헤더의 Token을 확인하고 Token에 문제가 있다면 에러 응답을 뱉고 문제가 없다면 요청에 맞는 서비스로 요청을 라우트 시킵니다. <br/>
cors 관련 처리를 해두었고 로그 필터를 작성하여 요청에 대한 로그를 찍도록 하였습니다. <br/>

## link to another repo
eureka server : https://github.com/ekgpgdi/devit-eureka-server  <br/>
gateway : https://github.com/ekgpgdi/devit-gateway <br/>
certification : https://github.com/ekgpgdi/devit-certification-service <br/>
board : https://github.com/kimziaco/devit-board <br/>
user : https://github.com/eet43/devit-user <br/>
chat : https://github.com/eet43/devit-chat <br/>
