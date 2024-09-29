import React from 'react';
import NavBar from './NavBar'; // 네비게이션 바 컴포넌트 임포트
import '../styles/MyPage.css'; // 스타일 파일 임포트

const MyPage = () => {
  return (
    <div>
      <NavBar /> {/* 네비게이션 바 */}
      <div className="my-page">
        {/* 프로필 섹션 */}
        <div className="profile-section">
          <div className="profile-picture">
            <img src="/path/to/profile-picture" alt="Profile" />
          </div>
          <div className="profile-info">
            <h2>USER</h2>
            <button className="logout-button">로그아웃</button>
            <button className="edit-button">개인정보 수정</button>
          </div>
        </div>

        {/* 찜한 숙소 섹션 */}
        <div className="liked-section">
          <h3>찜한 숙소</h3>
          <div className="liked-items">
            {[...Array(4)].map((_, i) => (
              <div className="liked-item" key={i}>
                <img src="/path/to/image" alt="Liked Hotel" />
                <div className="item-info">
                  <h4>호텔 이름</h4>
                  <p>17만원 / night</p>
                </div>
              </div>
            ))}
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
