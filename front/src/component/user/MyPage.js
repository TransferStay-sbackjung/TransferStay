import React, {useEffect,useState} from 'react';
import NavBar from '../NavBar'; // 네비게이션 바 컴포넌트 임포트
import '../../styles/user/MyPage.css';
import {useNavigate} from "react-router-dom"; // 스타일 파일 임포트

const MyPage = () => {
  const navigate = useNavigate();

  // 사용자 정보 상태 관리
  const [userInfo, setUserInfo] = useState({
    userId: '',
    email: '',
    phone: ''
  });

  const [likedPosts, setLikedPosts] = useState([]); // 좋아요한 게시글 상태

  useEffect(() => {
    const checkLoginStatus = async () => {
      const token = localStorage.getItem('token');
      if(!token){
        navigate("/login");
      }else {
        try {
          const infoResponse = await fetch("http://localhost:8080/api/v1/user/",{
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          if(infoResponse.ok){
            const response = await infoResponse.json();
            setUserInfo({
              userId: response.data.userId,
              email: response.data.email,
              phone: response.data.phone,
            });
          }else{
            console.error("Failed to fetch User info");
          }

          // 사용자가 좋아요한 게시글 데이터 불러오기 (API 호출)
          const likedPostsResponse = await fetch("http://localhost:8080/api/v1/user/liked-posts", {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          if(likedPostsResponse.ok){
            const likedPostData = await likedPostsResponse.json();
            setLikedPosts(likedPostData.data);
          }else{
            console.error("Failed to fetch like data.");
          }

        }catch(error){
          console.error("Error fetching User info",error);
        }
      }
    };

    checkLoginStatus();
  }, [navigate()]);


  return (
    <div>
      <NavBar /> {/* 네비게이션 바 */}
      <div className="my-page">
        {/* 프로필 섹션 */}
        <div className="profile-section">
          <div className="profile-picture">
            <img src="/path/to/profile-picture" alt="Profile"/>
          </div>
          <div className="profile-info">
            <p>Email: {userInfo.email}</p> {/* 사용자 이메일 표시 */}
            <p>Phone: {userInfo.phone}</p> {/* 사용자 전화번호 표시 */}
            <button className="edit-button">개인정보 수정</button>
          </div>
        </div>

        {/* 찜한 게시글 섹션 */}
        <div className="liked-section">
          <h3>찜한 게시글</h3>
          <div className="liked-items">
            {likedPosts.length > 0 ? (
                likedPosts.map((post) => (
                    <div className="liked-item" key={post.id}>
                      <h4>{post.title}</h4>
                      <p>{post.description}</p>
                      <p>가격: {post.price} 원</p>
                      <p>체크인: {post.checkInDate}</p>
                      <p>체크아웃: {post.checkOutDate}</p>
                      <p>예약 플랫폼: {post.reservationPlatform}</p>
                      <p>상태: {post.status}</p>
                      <p>경매 여부: {post.auction ? "경매중" : "일반"}</p>
                    </div>
                ))
            ) : (
                <p>찜한 게시글이 없습니다.</p>
            )}
          </div>
        </div>

        {/* 예치금 잔액 섹션 */}
        <div className="balance-section">
          <h3>예치금 잔액</h3>
          <p>200,000 원</p>
          <div className="balance-buttons">
            <button className="balance-button">충전하기</button>
            <button className="balance-button">환급하기</button>
          </div>
        </div>

        {/* 내가 올린 글, 경매 참여 중인 글 섹션 */}
        <div className="my-posts-section">
          <div className="my-posts">
            <h3>내가 올린 글</h3>
            <button className="post-button">글 보러가기</button>
          </div>
          <div className="my-auction-posts">
            <h3>경매 참여 중인 글</h3>
            <button className="post-button">참여 중인 글 보기</button>
          </div>
        </div>

        {/* 문의하기 섹션 */}
        <div className="inquiry-section">
          <h3>문의하기</h3>
          <button className="inquiry-button">문의 작성하기</button>
        </div>
      </div>

      <footer className="footer">
        <button>Contact Us</button>
        <button>About Us</button>
        <button>Terms of Service</button>
      </footer>
    </div>
  );
};

export default MyPage;
