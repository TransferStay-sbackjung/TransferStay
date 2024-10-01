import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

// mainPage
import MainPage from './component/MainPage';

// auth
import LoginPage from './component/auth/Login';
import EmailLogin from './component/auth/EmailLogin';
import SignUp from './component/auth/SignUp';
import AuthCallback from './component/auth/AuthCallback';  // AuthCallback 추가

// post
import WritePost from './component/post/WritePost'; // 게시글 작성 페이지 컴포넌트
import AllPosts from './component/post/AllPosts'; // 전체 게시글 조회 컴포넌트
import SearchResults from './component/SearchResults'; // 게시글 검색 결과 컴포넌트
import PostDetails from './component/post/PostDetails';


// myPage
import MyPage from './component/user/MyPage'; // 마이페이지 컴포넌트
import BalanceCharge from './component/user/BalanceCharge'; // 마이페이지 컴포넌트
import BalanceRefund from './component/user/BalanceRefund'; // 마이페이지 컴포넌트
import UserPostPage from './component/user/UserPostPage';
import UserAuctionPage from "./component/user/UserAuctionPage";

// auction
import AuctionPosts from './component/auction/AuctionPosts'; // 전체 경매글 조회 컴포넌트
import AuctionDetails from "./component/auction/AuctionDetails";

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/auth/:token" element={<AuthCallback />} /> {/* 닫는 태그 수정 */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/search" element={<SearchResults />} />
        <Route path="/email-login" element={<EmailLogin />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/write-post" element={<WritePost />} />
        <Route path="/my-page" element={<MyPage />} />
        <Route path="/all-posts" element={<AllPosts />} />
        <Route path="/auction-posts" element={<AuctionPosts />} />
        <Route path="/auctions/:auctionId" element={<AuctionDetails />} />
        <Route path="/balanceCharge" element={<BalanceCharge />} />
        <Route path="/balanceRefund" element={<BalanceRefund />} />
        <Route path="/userPostPage" element={<UserPostPage />} />
        <Route path="/userAuctionPostPage" element={<UserAuctionPage />} />
        <Route path="/posts/:postId" element={<PostDetails  />} />
      </Routes>
    </Router>
  );
};

export const API_BASE_URL = process.env.REACT_APP_API_ROOT;
export const HOME_URL = process.env.REACT_APP_HOME_URL;


export default App;
