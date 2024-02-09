import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './account-transaction.reducer';

export const AccountTransactionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const accountTransactionEntity = useAppSelector(state => state.accountTransaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="accountTransactionDetailsHeading">
          <Translate contentKey="blockchainApplicationApp.accountTransaction.detail.title">AccountTransaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{accountTransactionEntity.id}</dd>
          <dt>
            <span id="transactionName">
              <Translate contentKey="blockchainApplicationApp.accountTransaction.transactionName">Transaction Name</Translate>
            </span>
          </dt>
          <dd>{accountTransactionEntity.transactionName}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="blockchainApplicationApp.accountTransaction.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{accountTransactionEntity.amount}</dd>
          <dt>
            <Translate contentKey="blockchainApplicationApp.accountTransaction.clientAccount">Client Account</Translate>
          </dt>
          <dd>{accountTransactionEntity.clientAccount ? accountTransactionEntity.clientAccount.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/account-transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/account-transaction/${accountTransactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AccountTransactionDetail;
