import React, { useEffect, useState } from 'react';
import NavBar from '../NavBar'; // 네비게이션 바 컴포넌트 임포트
import '../../styles/user/UserAuctionPage.css'; // 스타일 파일 임포트
import { useNavigate } from "react-router-dom";

const UserAuctionPage = () => {
    const navigate = useNavigate();

    const [userAuctions, setUserAuctions] = useState([]); // 내가 참여 중인 경매 상태
    const [loading, setLoading] = useState(true); // 로딩 상태

    useEffect(() => {
        const fetchUserAuctions = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
            } else {
                try {
                    const response = await fetch("http://localhost:8080/api/v1/user/auction-posts", {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });

                    if (response.ok) {
                        const data = await response.json();
                        setUserAuctions(data.data); // 서버에서 받아온 경매 목록 설정
                    } else {
                        console.error("Failed to fetch user auctions");
                    }
                } catch (error) {
                    console.error("Error fetching user auctions", error);
                } finally {
                    setLoading(false); // 데이터 로드 후 로딩 상태 종료
                }
            }
        };

        fetchUserAuctions();
    }, [navigate]);

    if (loading) {
        return <p>로딩 중...</p>;
    }

    return (
        <div>
            <NavBar /> {/* 네비게이션 바 */}
            <div className="user-auctions-page">
                <h2>내가 참여 중인 경매</h2>
                <div className="user-auctions-list">
                    {userAuctions.length > 0 ? (
                        userAuctions.map((auction) => (
                            <div className="user-auction-item" key={auction.auctionId}>
                                <h3>경매 ID: {auction.auctionId}</h3>
                                <p>게시글 ID: {auction.postId}</p>
                                <p>시작 가격: {auction.startPrice.toLocaleString()} 원</p>
                                <p>마감 시간: {new Date(auction.deadline).toLocaleString()}</p>
                                <p>경매 상태: {auction.status}</p>
                                {auction.winningBidderId && (
                                    <p>현재 최고 입찰가: {auction.winningPrice?.toLocaleString() || '없음'} 원</p>
                                )}
                                <button onClick={() => navigate(`/auctions/${auction.auctionId}`)}>상세보기</button>
                            </div>
                        ))
                    ) : (
                        <p>참여 중인 경매가 없습니다.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default UserAuctionPage;
