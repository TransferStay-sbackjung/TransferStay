import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../styles/post/PostDetails.css'; // 스타일 파일 임포트
import NavBar from '../NavBar';

const PostDetails = () => {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [likeSuccess, setLikeSuccess] = useState(false);
  const [isLiked, setIsLiked] = useState(false);
  const navigate = useNavigate();

  const getStatusText = (status) => {
    switch (status) {
      case 'ACTIVE':
        return '판매 중';
      case 'TRANSACTION_IN_PROGRESS':
        return '거래 중';
      case 'PAYMENT_IN_PROGRESS':
        return '결제 진행 중';
      case 'TRANSACTION_COMPLETED':
        return '거래 완료';
      case 'DELETED':
        return '삭제됨';
      default:
        return '알 수 없음';
    }
  };

  const isActionDisabled = (status) => {
    return status !== 'ACTIVE';
  };

  useEffect(() => {
    const fetchPostAndLikeStatus = async () => {
      try {
        const token = localStorage.getItem('token');
        const postResponse = await axios.get(`http://localhost:8080/api/v1/assignment-posts/${postId}`);
        setPost(postResponse.data.data);
        setIsLoading(false);

        if (token) {
          const likeResponse = await axios.get(`http://localhost:8080/api/v1/assignment-posts/${postId}/likes`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          setIsLiked(likeResponse.data.data.isLiked);
        }
      } catch (err) {
        console.error('Failed to fetch post or like status:', err);
        setError(err);
        setIsLoading(false);
      }
    };

    fetchPostAndLikeStatus();
  }, [postId]);

  const handleLike = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다. 로그인 페이지로 이동합니다.');
      navigate('/login');
      return;
    }

    try {
      let response;
      if (isLiked) {
        response = await axios.delete(
          `http://localhost:8080/api/v1/assignment-posts/${postId}/likes`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
      } else {
        response = await axios.post(
          `http://localhost:8080/api/v1/assignment-posts/${postId}/likes`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
      }

      if (response.status === 200) {
        setIsLiked(!isLiked);
        setLikeSuccess(true);
      }
    } catch (err) {
      console.error('Failed to like/unlike post:', err);
      setLikeSuccess(false);
    }
  };

  if (isLoading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <p>Error: {error.message}</p>;
  }

  return (
    <div>
      <NavBar />
      <div className="post-detail">
        <div className="post-content">
          <div className="post-info">
            <h1>{post.title}</h1>
            <p className="price">{post.price.toLocaleString()}원</p>
            <p><span>숙소 위치:</span> {post.locationDepth1}, {post.locationDepth2}</p>
            <p><span>체크인:</span> {post.checkInDate}</p>
            <p><span>체크아웃:</span> {post.checkOutDate}</p>
            <p><span>인원:</span> {post.personnel}명</p>
            <p><span>예약 플랫폼:</span> {post.reservationPlatform}</p>
            <p className={`status ${post.status.toLowerCase().replace('_', '-')}`}>
              {getStatusText(post.status)}
            </p>
            {post.auction && <p className="auction-status">경매 글입니다</p>}
            <div className="button-group">
              <button className="wish-button" onClick={handleLike} disabled={isActionDisabled(post.status)}>
                {isLiked ? '찜 취소' : '찜하기'}
              </button>
              <button className="buy-button" disabled={isActionDisabled(post.status)}>
                구매하기
              </button>
            </div>
            {likeSuccess && <p className="like-success">{isLiked ? '찜이 완료되었습니다!' : '찜이 취소되었습니다!'}</p>}
          </div>
        </div>
        <div className="product-info">
          <h2>상품 정보</h2>
          <div className="description">
            <p>{post.description}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PostDetails;



