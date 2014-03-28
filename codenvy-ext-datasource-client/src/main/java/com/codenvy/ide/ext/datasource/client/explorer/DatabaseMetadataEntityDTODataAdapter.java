/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.ext.datasource.client.explorer;

import java.util.Collection;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTODataAdapter.EntityTreeNode;
import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.TableDTO;
import com.codenvy.ide.ui.tree.NodeDataAdapter;
import com.codenvy.ide.ui.tree.TreeNodeElement;

public class DatabaseMetadataEntityDTODataAdapter implements NodeDataAdapter<EntityTreeNode> {

    @Override
    public int compare(EntityTreeNode a, EntityTreeNode b) {
        if (a.getData() == null) {
            return 0;
        }
        if (b.getData() == null) {
            return 1;
        }
        return a.getData().getName().compareTo(b.getData().getName());
    }

    @Override
    public boolean hasChildren(final EntityTreeNode node) {
        if (node.getData() instanceof ColumnDTO) {
            return false;
        }
        return true;
    }

    @Override
    public Array<EntityTreeNode> getChildren(final EntityTreeNode node) {
        final DatabaseMetadataEntityDTO data = node.getData();
        if (data instanceof ColumnDTO) {
            return null;
        }

        Array<EntityTreeNode> children = Collections.<EntityTreeNode> createArray();
        if (data instanceof DatabaseDTO) {
            Collection<SchemaDTO> schemas = ((DatabaseDTO)data).getSchemas()
                                                               .values();
            for (SchemaDTO schemaDTO : schemas) {
                EntityTreeNode newChild = new EntityTreeNode(node, schemaDTO);
                children.add(newChild);
            }
        }
        if (data instanceof SchemaDTO) {
            Collection<TableDTO> tables = ((SchemaDTO)data).getTables()
                                                           .values();
            for (TableDTO tableDTO : tables) {
                EntityTreeNode newChild = new EntityTreeNode(node, tableDTO);
                children.add(newChild);
            }
        }
        if (data instanceof TableDTO) {
            Collection<ColumnDTO> columns = ((TableDTO)data).getColumns()
                                                            .values();
            for (ColumnDTO columnDTO : columns) {
                EntityTreeNode newChild = new EntityTreeNode(node, columnDTO);
                children.add(newChild);
            }
        }

        return children;
    }

    @Override
    public String getNodeId(final EntityTreeNode node) {
        return node.getData().getLookupKey();
    }

    @Override
    public String getNodeName(final EntityTreeNode node) {
        return node.getData().getName();
    }

    @Override
    public EntityTreeNode getParent(final EntityTreeNode node) {
        return node.getParent();
    }

    @Override
    public void setNodeName(final EntityTreeNode node, final String name) {
        // correct implementation : do nothing
    }

    @Override
    public TreeNodeElement<EntityTreeNode> getRenderedTreeNode(final EntityTreeNode node) {
        if (node == null) {
            return null;
        }
        return node.getTreeNodeElement();
    }

    @Override
    public void setRenderedTreeNode(final EntityTreeNode node,
                                    final TreeNodeElement<EntityTreeNode> renderedNode) {
        node.setTreeNodeElement(renderedNode);
    }

    @Override
    public EntityTreeNode getDragDropTarget(final EntityTreeNode node) {
        return null;
    }

    @Override
    public Array<String> getNodePath(EntityTreeNode node) {
        return null;
    }

    @Override
    public EntityTreeNode getNodeByPath(final EntityTreeNode root,
                                        final Array<String> relativeNodePath) {
        return null;
    }

    /**
     * Data node fot the database entity tree.
     * 
     * @author "Mickaël Leduque"
     */
    public static class EntityTreeNode {

        /** The parent of this node (null for root). */
        private final EntityTreeNode            parent;

        /** The entity data for this node (schema, table, column DTO). */
        private final DatabaseMetadataEntityDTO data;

        /** The UI tree node. */
        private TreeNodeElement<EntityTreeNode> treeNodeElement = null;

        public EntityTreeNode(EntityTreeNode parent, DatabaseMetadataEntityDTO data) {
            super();
            this.parent = parent;
            this.data = data;
        }

        public EntityTreeNode getParent() {
            return parent;
        }

        public DatabaseMetadataEntityDTO getData() {
            return data;
        }

        public TreeNodeElement<EntityTreeNode> getTreeNodeElement() {
            return treeNodeElement;
        }

        public void setTreeNodeElement(TreeNodeElement<EntityTreeNode> treeNodeElement) {
            this.treeNodeElement = treeNodeElement;
        }
    }
}
