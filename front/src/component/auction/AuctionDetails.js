import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../styles/auction/AuctionDetails.css'; // 스타일 파일 임포트
import NavBar from '../NavBar';
import { API_BASE_URL } from "../../App";

const AuctionDetails = () => {
    const { auctionId } = useParams(); // 경매 ID를 경로에서 추출
    const [auction, setAuction] = useState(null); // 경매 데이터를 저장할 상태
    const [isLoading, setIsLoading] = useState(true); // 로딩 상태 관리
    const [error, setError] = useState(null); // 오류 상태 관리
    const navigate = useNavigate(); // 페이지 이동을 위한 네비게이션 훅

    useEffect(() => {
        const fetchAuctionDetails = async () => {
            const token = localStorage.getItem('token'); // localStorage에서 토큰을 가져옴
            try {
                const response = await axios.get(`${API_BASE_URL}/api/v1/auction/details/${auctionId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}` // Authorization 헤더에 토큰 추가
                    }
                });
                setAuction(response.data.data);
                setIsLoading(false);
            } catch (err) {
                console.error('Failed to fetch auction details:', err);
                setError(err);
                setIsLoading(false);
            }
        };

        fetchAuctionDetails();
    }, [auctionId]);

    const handleBid = () => {
        // 응찰 로직 구현
        alert("응찰 기능을 여기에 구현하세요.");
    };

    const handlePurchase = () => {
        // 즉시 구매 로직 구현
        alert("즉시 구매 기능을 여기에 구현하세요.");
    };

    if (isLoading) {
        return <p>로딩 중...</p>;
    }

    if (error) {
        return <p>오류가 발생했습니다: {error.message}</p>;
    }

    return (
        <div>
            <NavBar />
            <div className="auction-detail">
                <div className="auction-info">
                    <h1>경매 ID: {auction.auctionId}</h1>
                    <p>경매 시작 시간: {new Date(auction.startTime).toLocaleString()}</p>
                    <p>경매 마감 시간: {new Date(auction.deadline).toLocaleString()}</p>
                    <p>시작 가격: {auction.startPrice.toLocaleString()} 원</p>
                    <p>현재 최고 입찰가: {auction.winningPrice ? auction.winningPrice.toLocaleString() : '입찰 없음'} 원</p>
                    <p>경매 상태: {auction.status === "BID_FAIL" ? "입찰 실패" : auction.status}</p>

                    <h2>입찰자 목록</h2>
                    {auction.bidders.length > 0 ? (
                        auction.bidders.map((bidder) => (
                            <div key={bidder.bidderId} className="bidder-info">
                                <p>입찰자 ID: {bidder.bidderId}</p>
                                <p>제안 가격: {bidder.suggestPrice.toLocaleString()} 원</p>
                                <p>입찰 시간: {new Date(bidder.createdAt).toLocaleString()}</p>
                            </div>
                        ))
                    ) : (
                        <p>입찰자가 없습니다.</p>
                    )}

                    <div className="button-group">
                        <button className="bid-button" onClick={handleBid}>
                            응찰하기
                        </button>
                        <button className="buy-button" onClick={handlePurchase}>
                            즉시 구매하기
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AuctionDetails;
