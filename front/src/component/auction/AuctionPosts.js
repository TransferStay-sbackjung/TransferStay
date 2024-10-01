import React, { useEffect, useState } from 'react';
import NavBar from '../NavBar'; // 네비게이션 바 컴포넌트 임포트
import '../../styles/auction/AuctionPost.css'; // 경매 리스트 스타일 파일
import { useNavigate } from "react-router-dom";

const AuctionList = () => {
    const [auctions, setAuctions] = useState([]); // 경매 리스트를 저장할 상태
    const [loading, setLoading] = useState(true); // 로딩 상태 관리
    const [error, setError] = useState(null); // 오류 관리
    const navigate = useNavigate(); // 페이지 이동을 위한 네비게이션 훅

    useEffect(() => {
        // 경매 데이터를 API로부터 가져오는 함수
        const fetchAuctions = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/v1/auction/'); // 적절한 API 엔드포인트로 변경
                const result = await response.json();
                setAuctions(result.data.content); // API 응답에서 경매 데이터 설정
                setLoading(false); // 데이터 로드 완료 후 로딩 중지
            } catch (err) {
                setError('경매 데이터를 불러오는 중 오류가 발생했습니다.'); // 오류 처리
                setLoading(false); // 로딩 중지
            }
        };

        fetchAuctions(); // 컴포넌트 마운트 시 API 호출
    }, []);

    if (loading) {
        return <div>로딩 중...</div>; // 로딩 중일 때 표시
    }

    if (error) {
        return <div>{error}</div>; // 오류 발생 시 메시지 표시
    }

    // 경매 목록 클릭 시 상세 페이지로 이동
    const handleAuctionClick = (auctionId) => {
        navigate(`/auction/${auctionId}`); // 해당 경매의 디테일 페이지로 이동
    };

    return (
        <div className="main-page">
            {/* 네비게이션 바 추가 */}
            <NavBar />
            <h1>경매 목록</h1>
            <div className="auctions-list">
                {auctions.length > 0 ? (
                    auctions.map((auction) => (
                        <div
                            key={auction.auctionId}
                            className="auction-item"
                            onClick={() => handleAuctionClick(auction.auctionId)} // 경매 클릭 시 상세 페이지로 이동
                        >
                            <div className="auction-info">
                                <h2>경매 ID: {auction.auctionId}</h2>
                                <p>시작 가격: {auction.startPrice.toLocaleString()} 원</p>
                                <p>현재 최고 입찰가: {auction.winningPrice.toLocaleString()} 원</p>
                                <p>경매 상태: {auction.status}</p>
                                <p>마감 시간: {new Date(auction.deadline).toLocaleString()}</p>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>경매 목록이 없습니다.</p>
                )}
            </div>
        </div>
    );
};

export default AuctionList;
